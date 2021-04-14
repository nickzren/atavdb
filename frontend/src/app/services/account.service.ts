import { HttpClient } from '@angular/common/http';
import { HttpParams } from "@angular/common/http";
import { Injectable } from '@angular/core';

import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  constructor(private http: HttpClient) { }

  async reset() {
    const authenticated = await this.http.get(`${environment.apiUrl}/authenticated`).toPromise();
    if (!authenticated) {
      localStorage.clear();
    }
  }

  // verify CUMC MC account
  async authenticate(username: string, password: string) {
    let url = `${environment.apiUrl}/authenticate`;
    let params = new HttpParams();
    params = params.append('username', username);
    params = params.append('password', password);

    const authenticate = await this.http.get<boolean>(url, { params: params }).toPromise();

    if (authenticate) {
      localStorage.setItem('authenticated', 'true');
      return true;
    }

    return false;
  }

  // verify sequence account
  async authorize(username: string) {
    let url = `${environment.apiUrl}/authorize`;
    let params = new HttpParams();
    params = params.append('username', username);

    const authorize = await this.http.get<boolean>(url, { params: params }).toPromise();

    if (authorize) {
      localStorage.setItem('authorized', 'true');
    }
  }

  isAuthenticated(): boolean {
    return localStorage.getItem('authenticated') != null;
  }

  isAuthorized(): boolean {
    return localStorage.getItem('authorized') != null;
  }

  async signout() {
    // signout from backend api
    await this.http.get(`${environment.apiUrl}/signout`).toPromise();

    localStorage.clear();
  }
}
