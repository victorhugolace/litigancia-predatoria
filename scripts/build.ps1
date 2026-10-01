param(
    [ValidateSet('app', 'frontend', 'api-test', 'e2e')]
    [string] $Target = 'app'
)
$ErrorActionPreference = 'Stop'
$taskRepo = Split-Path -Parent $PSScriptRoot
$taskBuilder = 'litigancia-limitado'
$taskBuildError = $null
Push-Location -LiteralPath $taskRepo
try {
    & docker buildx inspect $taskBuilder *> $null
    if ($LASTEXITCODE -ne 0) {
        & docker buildx create --name $taskBuilder --driver docker-container --driver-opt memory=2g,memory-swap=2g,cpu-period=100000,cpu-quota=100000 --buildkitd-config docker/buildkitd.toml
        if ($LASTEXITCODE -ne 0) { throw 'Nao foi possivel criar o builder limitado' }
    }
    $taskBuilds = switch ($Target) {
        'frontend' { @(
            @{ Context = 'frontend'; File = 'frontend/Dockerfile'; Tag = 'litigancia-predatoria-frontend' }
        ) }
        'app' { @(
            @{ Context = 'backend'; File = 'backend/Dockerfile'; Tag = 'litigancia-predatoria-backend' },
            @{ Context = 'frontend'; File = 'frontend/Dockerfile'; Tag = 'litigancia-predatoria-frontend' }
        ) }
        'api-test' { @(
            @{ Context = 'backend'; File = 'backend/Dockerfile'; Tag = 'litigancia-validacao-backend-test'; Stage = 'test' }
        ) }
        'e2e' { @(
            @{ Context = 'backend'; File = 'backend/Dockerfile'; Tag = 'litigancia-e2e-backend' },
            @{ Context = 'frontend'; File = 'frontend/Dockerfile'; Tag = 'litigancia-e2e-frontend' },
            @{ Context = 'frontend'; File = 'frontend/Dockerfile.e2e'; Tag = 'litigancia-e2e-browser-test' }
        ) }
    }
    foreach ($taskBuild in $taskBuilds) {
        $taskArgs = @('buildx', 'build', '--builder', $taskBuilder, '--load', '--progress=plain', '-f', $taskBuild.File, '-t', $taskBuild.Tag)
        if ($taskBuild.Stage) { $taskArgs += @('--target', $taskBuild.Stage) }
        $taskArgs += $taskBuild.Context
        & docker @taskArgs
        if ($LASTEXITCODE -ne 0) { throw "Falha no build de $($taskBuild.Tag)" }
    }
}
catch { $taskBuildError = $_ }
finally {
    & docker buildx stop $taskBuilder
    Pop-Location
}
if ($taskBuildError) {
    Write-Error -ErrorRecord $taskBuildError -ErrorAction Continue
    exit 1
}
