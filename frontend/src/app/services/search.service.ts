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

  search(query: string): Observable<any> {
    let url = `${environment.apiUrl}/search`;
    let params = new HttpParams();
    params = params.append('query', query);

    return this.http.get(url, { params: params });
  }

/*   searchByGene(query: string): Observable<any> {
    let url = `${environment.apiUrl}/gene/${query}`;
    let params = new HttpParams();

    return this.http.get(url, { params: params });
  }

  searchByRegion(query: string): Observable<any> {
    let url = `${environment.apiUrl}/gene/${query}`;
    let params = new HttpParams();

    return this.http.get(url, { params: params });
  } */
}
