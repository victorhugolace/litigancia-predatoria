import { CommonModule } from '@angular/common';
import { Component, signal } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { DenunciaService } from '../../core/services/denuncia.service';
import { Tipologia, TIPOLOGIA_LABELS } from '../../core/models/denuncia.model';
import { textoObrigatorio, oabValida, documentoValido, processosDistintos } from '../../core/models/form.validators';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { AuthService } from '../../core/services/auth.service';

const CNJ_PATTERN = /^\d{7}-\d{2}\.\d{4}\.\d\.\d{2}\.\d{4}$/;

interface StepDef {
  titulo: string;
  eyebrow: string;
}

@Component({
  selector: 'lp-denuncia-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './denuncia-form.component.html',
  styleUrl: './denuncia-form.component.scss'
})
export class DenunciaFormComponent {
  readonly steps: StepDef[] = [
    { eyebrow: 'Passo 1', titulo: 'Identificação da origem' },
    { eyebrow: 'Passo 2', titulo: 'Alvo da suspeita' },
    { eyebrow: 'Passo 3', titulo: 'Tipologia da ação' },
    { eyebrow: 'Passo 4', titulo: 'Materialidade das evidências' }
  ];

  readonly tipologiaOptions = Object.values(Tipologia).map(valor => ({
    valor,
    label: TIPOLOGIA_LABELS[valor]
  }));

  currentStep = signal(0);
  enviando = signal(false);
  protocoloGerado = signal<string | null>(null);
  erroEnvio = signal<string | null>(null);

  form: FormGroup;

  constructor(private fb: FormBuilder, private denunciaService: DenunciaService, private router: Router, public auth: AuthService) {
    this.form = this.fb.group({
      comarca: ['', [textoObrigatorio, Validators.maxLength(120)]],
      nomeComunicante: ['', [textoObrigatorio, Validators.maxLength(150)]],
      matriculaComunicante: ['', [textoObrigatorio, Validators.maxLength(30)]],
      oabAdvogado: ['', [textoObrigatorio, oabValida]],
      cpfCnpjParte: ['', documentoValido],
      tipologia: [null as Tipologia | null, Validators.required],
      tipologiaOutros: ['', Validators.maxLength(120)],
      processosParadigmas: this.fb.array(
        [this.novoProcesso(), this.novoProcesso(), this.novoProcesso()],
        [Validators.minLength(3), Validators.maxLength(5), processosDistintos]
      ),
      relato: ['', [textoObrigatorio, Validators.maxLength(500)]]
    });
    this.form.get('tipologia')!.valueChanges.pipe(takeUntilDestroyed()).subscribe(value => {
      const outros = this.form.get('tipologiaOutros')!;
      outros.setValidators(value === Tipologia.OUTROS
        ? [textoObrigatorio, Validators.maxLength(120)] : [Validators.maxLength(120)]);
      if (value !== Tipologia.OUTROS) outros.setValue('', { emitEvent: false });
      outros.updateValueAndValidity();
    });
  }

  get processosArray(): FormArray {
    return this.form.get('processosParadigmas') as FormArray;
  }

  private novoProcesso() {
    return this.fb.control('', [Validators.required, Validators.pattern(CNJ_PATTERN)]);
  }

  adicionarProcesso(): void {
    if (this.processosArray.length < 5) {
      this.processosArray.push(this.novoProcesso());
    }
  }

  removerProcesso(index: number): void {
    if (this.processosArray.length > 3) {
      this.processosArray.removeAt(index);
    }
  }

  get relatoRestante(): number {
    return 500 - (this.form.get('relato')?.value?.length ?? 0);
  }

  get isOutros(): boolean {
    return this.form.get('tipologia')?.value === Tipologia.OUTROS;
  }

  camposDoPasso(step: number): string[] {
    switch (step) {
      case 0: return ['comarca', 'nomeComunicante', 'matriculaComunicante'];
      case 1: return ['oabAdvogado', 'cpfCnpjParte'];
      case 2: return ['tipologia', 'tipologiaOutros'];
      case 3: return ['relato'];
      default: return [];
    }
  }

  passoValido(step: number): boolean {
    if (step === 3) {
      return this.processosArray.valid && (this.form.get('relato')?.valid ?? false);
    }
    return this.camposDoPasso(step).every(campo => this.form.get(campo)?.valid ?? true);
  }

  irParaProximo(): void {
    this.camposDoPasso(this.currentStep()).forEach(campo => this.form.get(campo)?.markAsTouched());
    if (this.currentStep() === 3) {
      this.processosArray.controls.forEach(c => c.markAsTouched());
      this.form.get('relato')?.markAsTouched();
    }
    if (this.passoValido(this.currentStep()) && this.currentStep() < this.steps.length - 1) {
      this.currentStep.set(this.currentStep() + 1);
    }
  }

  irParaAnterior(): void {
    if (this.currentStep() > 0) {
      this.currentStep.set(this.currentStep() - 1);
    }
  }

  irParaPasso(index: number): void {
    if (index <= this.currentStep() || this.todosPassosAnterioresValidos(index)) {
      this.currentStep.set(index);
    }
  }

  private todosPassosAnterioresValidos(ateIndex: number): boolean {
    for (let i = 0; i < ateIndex; i++) {
      if (!this.passoValido(i)) return false;
    }
    return true;
  }

  arquivosAnexados = signal<File[]>([]);
  erroArquivo = signal<string | null>(null);
  isDragging = signal(false);

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.adicionarArquivos(Array.from(input.files));
      input.value = '';
    }
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    this.isDragging.set(true);
  }

  onDragLeave(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    this.isDragging.set(false);
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    this.isDragging.set(false);
    if (event.dataTransfer?.files && event.dataTransfer.files.length > 0) {
      this.adicionarArquivos(Array.from(event.dataTransfer.files));
    }
  }

  adicionarArquivos(novosArquivos: File[]): void {
    const atuais = this.arquivosAnexados();
    this.erroArquivo.set(null);
    if (atuais.length + novosArquivos.length > 5) {
      this.erroArquivo.set('Selecione no máximo 5 PDFs.'); return;
    }
    if (novosArquivos.some(f => !f.name.toLowerCase().endsWith('.pdf') || f.size === 0 || f.size > 10 * 1024 * 1024)) {
      this.erroArquivo.set('Os arquivos devem ser PDFs com conteúdo e no máximo 10 MB cada.'); return;
    }
    this.arquivosAnexados.set([...atuais, ...novosArquivos]);
  }

  removerAnexo(index: number): void {
    const atuais = this.arquivosAnexados();
    this.arquivosAnexados.set(atuais.filter((_, i) => i !== index));
  }

  formatarTamanhoArquivo(bytes: number): string {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const tamanhos = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(1)) + ' ' + tamanhos[i];
  }

  enviar(): void {
    if (this.enviando()) return;
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      const invalidStep = this.steps.findIndex((_, index) => !this.passoValido(index));
      if (invalidStep >= 0) this.currentStep.set(invalidStep);
      return;
    }
    this.enviando.set(true);
    this.erroEnvio.set(null);

    const valor = this.form.value;
    this.denunciaService.criar({
      comarca: valor.comarca,
      nomeComunicante: valor.nomeComunicante,
      matriculaComunicante: valor.matriculaComunicante,
      oabAdvogado: valor.oabAdvogado,
      cpfCnpjParte: valor.cpfCnpjParte || undefined,
      tipologia: valor.tipologia,
      tipologiaOutros: valor.tipologiaOutros || undefined,
      processosParadigmas: valor.processosParadigmas,
      relato: valor.relato,
      anexos: []
    }, this.arquivosAnexados()).subscribe({
      next: resposta => {
        this.enviando.set(false);
        this.protocoloGerado.set(resposta.numeroProtocolo);
      },
      error: erro => {
        this.enviando.set(false);
        this.erroEnvio.set(
          erro?.error?.erros ? Object.values(erro.error.erros).join('; ') :
          erro?.error?.mensagem ?? 'Não foi possível registrar a denúncia. Tente novamente em instantes.'
        );
      }
    });
  }

  registrarNova(): void {
    this.protocoloGerado.set(null);
    this.currentStep.set(0);
    this.form.reset();
    this.arquivosAnexados.set([]);
    this.erroArquivo.set(null);
    this.processosArray.clear();
    this.processosArray.push(this.novoProcesso());
    this.processosArray.push(this.novoProcesso());
    this.processosArray.push(this.novoProcesso());
  }

  irParaPainel(): void {
    this.router.navigate(['/painel']);
  }

  acompanharCaso(): void {
    this.router.navigate(['/acompanhar'], { queryParams: { protocolo: this.protocoloGerado() } });
  }

  aplicarMascaraCNJ(index: number, event: Event): void {
    const input = event.target as HTMLInputElement;
    const valorFormatado = this.formatarCNJ(input.value);
    this.processosArray.at(index).setValue(valorFormatado, { emitEvent: false });
    input.value = valorFormatado;
  }

  aplicarMascaraCpfCnpj(event: Event): void {
    const input = event.target as HTMLInputElement;
    const valorFormatado = this.formatarCpfCnpj(input.value);
    this.form.get('cpfCnpjParte')?.setValue(valorFormatado, { emitEvent: false });
    input.value = valorFormatado;
  }

  formatarCNJ(valor: string): string {
    const v = valor.replace(/\D/g, '').slice(0, 20);
    if (v.length <= 7) return v;
    if (v.length <= 9) return `${v.slice(0, 7)}-${v.slice(7)}`;
    if (v.length <= 13) return `${v.slice(0, 7)}-${v.slice(7, 9)}.${v.slice(9)}`;
    if (v.length <= 14) return `${v.slice(0, 7)}-${v.slice(7, 9)}.${v.slice(9, 13)}.${v.slice(13)}`;
    if (v.length <= 16) return `${v.slice(0, 7)}-${v.slice(7, 9)}.${v.slice(9, 13)}.${v.slice(13, 14)}.${v.slice(14)}`;
    return `${v.slice(0, 7)}-${v.slice(7, 9)}.${v.slice(9, 13)}.${v.slice(13, 14)}.${v.slice(14, 16)}.${v.slice(16, 20)}`;
  }

  formatarCpfCnpj(valor: string): string {
    const v = valor.replace(/\D/g, '').slice(0, 14);
    if (v.length <= 11) {
      if (v.length <= 3) return v;
      if (v.length <= 6) return `${v.slice(0, 3)}.${v.slice(3)}`;
      if (v.length <= 9) return `${v.slice(0, 3)}.${v.slice(3, 6)}.${v.slice(6)}`;
      return `${v.slice(0, 3)}.${v.slice(3, 6)}.${v.slice(6, 9)}-${v.slice(9)}`;
    } else {
      if (v.length <= 12) return `${v.slice(0, 2)}.${v.slice(2, 5)}.${v.slice(5, 8)}/${v.slice(8)}`;
      return `${v.slice(0, 2)}.${v.slice(2, 5)}.${v.slice(5, 8)}/${v.slice(8, 12)}-${v.slice(12)}`;
    }
  }
}

