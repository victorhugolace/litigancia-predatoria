import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export const textoObrigatorio: ValidatorFn = control =>
  typeof control.value === 'string' && control.value.trim().length > 0 ? null : { required: true };

export const oabValida: ValidatorFn = control =>
  /^[A-Z]{2}[0-9]{1,10}[A-Z]?$/.test(String(control.value ?? '').toUpperCase().replace(/[\s/.-]/g, ''))
    ? null : { oab: true };

export function documentoValido(control: AbstractControl): ValidationErrors | null {
  const value = String(control.value ?? '').toUpperCase().replace(/[\s./-]/g, '');
  if (!value) return null;
  if (!/^(\d{11}|[A-Z0-9]{12}\d{2})$/.test(value) || new Set(value).size === 1) return { documento: true };
  for (let i = value.length - 2; i < value.length; i++) {
    let sum = 0;
    for (let j = 0; j < i; j++) {
      const weight = value.length === 11 ? i + 1 - j : (i - 1 - j) % 8 + 2;
      sum += (value.charCodeAt(j) - 48) * weight;
    }
    const remainder = sum % 11;
    if (Number(value[i]) !== (remainder < 2 ? 0 : 11 - remainder)) return { documento: true };
  }
  return null;
}

export const processosDistintos: ValidatorFn = control => {
  const values: string[] = control.value ?? [];
  return new Set(values.filter(Boolean)).size === values.filter(Boolean).length ? null : { duplicados: true };
};
