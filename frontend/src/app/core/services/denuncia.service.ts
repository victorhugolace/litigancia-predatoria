import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { DenunciaRequest, DenunciaResponse, StatusDenuncia, PaginaDenuncias, StatusUpdateRequest } from '../models/denuncia.model';

@Injectable({ providedIn: 'root' })
export class DenunciaService {
  private readonly baseUrl = `${environment.apiUrl}/denuncias`;

  constructor(private http: HttpClient) {}

  criar(request: DenunciaRequest): Observable<DenunciaResponse> {
    return this.http.post<DenunciaResponse>(this.baseUrl, request);
  }

  listar(status?: StatusDenuncia, pagina = 0, tamanho = 25): Observable<PaginaDenuncias> {
    let params = new HttpParams().set('pagina', pagina).set('tamanho', tamanho);
    if (status) {
      params = params.set('status', status);
    }
    return this.http.get<PaginaDenuncias>(this.baseUrl, { params });
  }

  buscar(id: number): Observable<DenunciaResponse> {
    return this.http.get<DenunciaResponse>(`${this.baseUrl}/${id}`);
  }

  atualizarStatus(id: number, payload: StatusUpdateRequest): Observable<DenunciaResponse> {
    return this.http.patch<DenunciaResponse>(`${this.baseUrl}/${id}/status`, payload);
  }
}
