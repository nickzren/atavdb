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
}
