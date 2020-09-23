import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SampleCountService {

  constructor(private http: HttpClient) {
  }

  get(): Observable<any> {
    return this.http.get('//localhost:8080/api/sample-count/');
  }
}
