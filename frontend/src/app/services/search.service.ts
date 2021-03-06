import { AccountService } from '../services/account.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  constructor(
    private http: HttpClient,
    private accountService: AccountService) { }

  querytype(query: string): Observable<any> {
    let url = `${environment.apiUrl}/querytype`;
    let params = new HttpParams();
    params = params.append('query', query);

    return this.http.get(url, { params: params });
  }

  search(
    queryType: string,
    query: string,
    phenotype: string,
    maf: string,
    isHighQualityVariant: string,
    isUltraRareVariant: string,
    isPubliclyAvailable: string,
    experimentId: string
  ): Observable<any> {
    let url = `${environment.apiUrl}/search`;
    let params = new HttpParams();

    if (query) {
      params = params.append('query', query);
    }

    if (phenotype) {
      params = params.append('phenotype', phenotype);
    }

    if (maf) {
      params = params.append('maf', maf);
    }

    if (queryType == 'gene' || queryType == 'region') {
      isHighQualityVariant = 'true';
    }
    if (isHighQualityVariant) {
      params = params.append('isHighQualityVariant', isHighQualityVariant);
    }

    if (isUltraRareVariant) {
      params = params.append('isUltraRareVariant', isUltraRareVariant);
    }

    if (!this.accountService.isAuthenticated()) {
      isPubliclyAvailable = 'true';
    }
    if (isPubliclyAvailable) {
      params = params.append('isPubliclyAvailable', isPubliclyAvailable);
    }

    if (experimentId) {
      params = params.append('experimentId', experimentId);
    }

    return this.http.get(url, { params: params });
  }
}
