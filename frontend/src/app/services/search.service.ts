import { HttpClient } from '@angular/common/http';
import { HttpParams } from "@angular/common/http";
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  constructor(private http: HttpClient) { }

  querytype(query: string): Observable<any> {
    let url = `${environment.apiUrl}/querytype`;
    let params = new HttpParams();
    params = params.append('query', query);

    return this.http.get(url, { params: params });
  }

  search(query: string): Observable<any> {
    let url = `${environment.apiUrl}/search`;
    let params = new HttpParams();
    params = params.append('query', query);

    return this.http.get(url, { params: params });
  }

  searchByGene(
    gene: string,
    phenotype: string,
    maf: string,
    isHighQualityVariant: string,
    isUltraRareVariant: string,
    isPublicAvailable: string
  ): Observable<any> {
    let url = `${environment.apiUrl}/gene/${gene}`;
    let params = new HttpParams();

    if (phenotype) {
      params = params.append('phenotype', phenotype);
    }
    if (maf) {
      params = params.append('maf', maf);
    }
    if (isHighQualityVariant) {
      params = params.append('isHighQualityVariant', isHighQualityVariant);
    }
    if (isUltraRareVariant) {
      params = params.append('isUltraRareVariant', isUltraRareVariant);
    }
    if (phenotype) {
      params = params.append('isPublicAvailable', isPublicAvailable);
    }

    return this.http.get(url, { params: params });
  }
}
