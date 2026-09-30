import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ComarcaItem, KpisResponse, RankingItem, TendenciaItem, TipologiaItem } from '../models/denuncia.model';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private readonly baseUrl = `${environment.apiUrl}/dashboard`;

  constructor(private http: HttpClient) {}

  kpis(periodo = 'ANO'): Observable<KpisResponse> {
    return this.http.get<KpisResponse>(`${this.baseUrl}/kpis`, { params: { periodo } });
  }

  ranking(periodo = 'ANO', porDocumento = false): Observable<RankingItem[]> {
    return this.http.get<RankingItem[]>(`${this.baseUrl}/ranking`, { params: { periodo, porDocumento } });
  }

  tipologia(periodo = 'ANO'): Observable<TipologiaItem[]> {
    return this.http.get<TipologiaItem[]>(`${this.baseUrl}/tipologia`, { params: { periodo } });
  }

  comarcas(periodo = 'ANO'): Observable<ComarcaItem[]> {
    return this.http.get<ComarcaItem[]>(`${this.baseUrl}/comarcas`, { params: { periodo } });
  }

  tendencia(periodo = 'ANO'): Observable<TendenciaItem[]> {
    return this.http.get<TendenciaItem[]>(`${this.baseUrl}/tendencia`, { params: { periodo } });
  }
}
