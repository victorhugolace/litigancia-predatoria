$ErrorActionPreference = 'Stop'
$taskRepo = Split-Path -Parent $PSScriptRoot
Push-Location -LiteralPath $taskRepo
try {
    foreach ($taskSuite in @(
        @{ Project = 'litigancia-validacao'; File = 'compose.test.yml'; Service = 'backend-test'; Target = 'api-test' },
        @{ Project = 'litigancia-e2e'; File = 'compose.e2e.yml'; Service = 'browser-test'; Target = 'e2e' }
    )) {
        try {
            & ./scripts/build.ps1 -Target $taskSuite.Target
            if ($LASTEXITCODE -ne 0) { throw "Falha na compilacao de $($taskSuite.Project)" }
            & docker compose -p $taskSuite.Project -f $taskSuite.File up --no-build --abort-on-container-exit --exit-code-from $taskSuite.Service
            if ($LASTEXITCODE -ne 0) { throw "Falha na suite $($taskSuite.Project), codigo $LASTEXITCODE" }
        }
        finally {
            & docker compose -p $taskSuite.Project -f $taskSuite.File down
            if ($LASTEXITCODE -ne 0) { Write-Warning "Nao foi possivel encerrar $($taskSuite.Project)" }
        }
    }
}
finally { Pop-Location }
