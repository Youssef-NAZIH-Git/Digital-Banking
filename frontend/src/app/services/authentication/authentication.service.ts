import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { jwtDecode } from 'jwt-decode';
import { environment } from '../../../environments/environment';

interface JwtPayload {
  sub: string;
  scope: string;
  exp: number;
  iat: number;
  iss: string;
}

@Injectable({ providedIn: 'root' })
export class AuthenticationService {
  isAuthenticated = false;
  username: string | null = null;
  roles: string[] = [];
  accessToken: string | null = null;

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<{ 'access-token': string }> {
    const body = new URLSearchParams();
    body.set('username', username);
    body.set('password', password);

    return this.http.post<{ 'access-token': string }>(
      `${environment.backendHost}/auth/login`,
      body.toString(),
      { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }
    );
  }

  loadProfile(data: { 'access-token': string }): void {
    this.accessToken = data['access-token'];
    const decoded = jwtDecode<JwtPayload>(this.accessToken);
    this.username = decoded.sub;
    this.roles = decoded.scope ? decoded.scope.split(' ') : [];
    this.isAuthenticated = true;
  }

  logout(): void {
    this.isAuthenticated = false;
    this.username = null;
    this.roles = [];
    this.accessToken = null;
  }

  hasRole(role: string): boolean {
    return this.roles.includes(role);
  }
}