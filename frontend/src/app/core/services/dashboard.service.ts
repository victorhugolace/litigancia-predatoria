import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ComarcaItem, KpisResponse, RankingItem, TendenciaItem, TipologiaItem } from '../models/denuncia.model';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private readonly baseUrl = `${environment.apiUrl}/dashboard`;

  constructor(private http: HttpClient) {}

  kpis(): Observable<KpisResponse> {
    return this.http.get<KpisResponse>(`${this.baseUrl}/kpis`);
  }

  ranking(): Observable<RankingItem[]> {
    return this.http.get<RankingItem[]>(`${this.baseUrl}/ranking`);
  }

  tipologia(): Observable<TipologiaItem[]> {
    return this.http.get<TipologiaItem[]>(`${this.baseUrl}/tipologia`);
  }

  comarcas(): Observable<ComarcaItem[]> {
    return this.http.get<ComarcaItem[]>(`${this.baseUrl}/comarcas`);
  }

  tendencia(): Observable<TendenciaItem[]> {
    return this.http.get<TendenciaItem[]>(`${this.baseUrl}/tendencia`);
  }
}
