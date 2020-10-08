import { HttpClient } from '@angular/common/http';
import { HttpParams } from "@angular/common/http";
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  constructor(private http: HttpClient) { }

  init() {
    this.http.get(`${environment.apiUrl}/authenticated`).toPromise().then(
      authenticated => {
        if (authenticated) {
          sessionStorage.setItem('authenticated', 'true');
        } else{
          sessionStorage.removeItem('authenticated');
        }
      }
    );

    this.http.get(`${environment.apiUrl}/authorized`).toPromise().then(
      authorized => {
        if (authorized) {
          sessionStorage.setItem('authorized', 'true');
        } else{
          sessionStorage.removeItem('authorized');
        }
      }
    );
  }

  // verify CUMC MC account
  authenticate(username: string, password: string) {
    let url = `${environment.apiUrl}/authenticate`;
    let params = new HttpParams();
    params = params.append('username', username);
    params = params.append('password', password);

    return this.http.get<Observable<boolean>>(url, { params: params });
  }

  // verify sequence account
  authorize(username: string) {
    let url = `${environment.apiUrl}/authorize`;
    let params = new HttpParams();
    params = params.append('username', username);
    this.http.get<Observable<boolean>>(url, { params: params })
      .subscribe(isValid => {
        if (isValid) {
          sessionStorage.setItem('authorized', 'true')
        }
      });
  }

  isAuthenticated() {
    return sessionStorage.getItem('authenticated') != null;
  }

  isAuthorized() {
    return sessionStorage.getItem('authorized') != null;
  }

  signout() {
    // signout from backend api
    this.http.get(`${environment.apiUrl}/signout`).toPromise();

    sessionStorage.clear();
  }
}
