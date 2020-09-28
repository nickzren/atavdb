import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SampleCountService {

  constructor(private http: HttpClient) {
  }

  get(): Observable<any> {
    return this.http.get(`${environment.apiUrl}/sample-count/`);
  }
}
