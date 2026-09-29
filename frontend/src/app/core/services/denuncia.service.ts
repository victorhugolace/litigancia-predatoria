import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { DenunciaRequest, DenunciaResponse, StatusDenuncia } from '../models/denuncia.model';

@Injectable({ providedIn: 'root' })
export class DenunciaService {
  private readonly baseUrl = `${environment.apiUrl}/denuncias`;

  constructor(private http: HttpClient) {}

  criar(request: DenunciaRequest): Observable<DenunciaResponse> {
    return this.http.post<DenunciaResponse>(this.baseUrl, request);
  }

  listar(status?: StatusDenuncia): Observable<DenunciaResponse[]> {
    let params = new HttpParams();
    if (status) {
      params = params.set('status', status);
    }
    return this.http.get<DenunciaResponse[]>(this.baseUrl, { params });
  }

  buscar(id: number): Observable<DenunciaResponse> {
    return this.http.get<DenunciaResponse>(`${this.baseUrl}/${id}`);
  }

  atualizarStatus(id: number, payload: Partial<{
    status: StatusDenuncia;
    volumeProcessualEstimado: number;
    comarcasAtingidas: number;
    notaTecnicaResumo: string;
  }>): Observable<DenunciaResponse> {
    return this.http.patch<DenunciaResponse>(`${this.baseUrl}/${id}/status`, payload);
  }
}
