import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SampleCountService {

  constructor(
    private http: HttpClient) {
  }

  get(phenotype: string, isPublicAvailable: string): Observable<any> {
    let url = `${environment.apiUrl}/sample-count/`;
    let params = new HttpParams();

    if (phenotype) {
      params = params.append('phenotype', phenotype);
    }
    if (isPublicAvailable) {
      params = params.append('isPublicAvailable', isPublicAvailable);
    }

    return this.http.get(url, { params: params });
  }
}
