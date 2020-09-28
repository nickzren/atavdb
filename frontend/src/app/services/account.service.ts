import { HttpClient } from '@angular/common/http';
import { HttpParams } from "@angular/common/http";
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  constructor(private http: HttpClient) { }

  // verify CUMC MC account
  authenticate(username: string, password: string) {
    let url = 'http://localhost:8080/api/authenticate';
    let params = new HttpParams();
    params = params.append('username', username);
    params = params.append('password', password);

    return this.http.get<Observable<boolean>>(url, { params: params });
  }

  // verify sequence account
  authorize(username: string) {
    let url = 'http://localhost:8080/api/authorize';
    let params = new HttpParams();
    params = params.append('username', username);
    this.http.get<Observable<boolean>>(url, { params: params })
      .subscribe(isValid => {
        if (isValid) {
          sessionStorage.setItem('authorizedUser', username)
        }
      });
  }

  isAuthenticated() {
    let user = sessionStorage.getItem('authenticatedUser')
    if (user === null) return false
    return true
  }

  isAuthorized() {
    let user = sessionStorage.getItem('authorizedUser')
    if (user === null) return false
    return true
  }

  signout() {
    // signout from backend api
    this.http.get('//localhost:8080/api/signout/');

    sessionStorage.clear();
  }
}
