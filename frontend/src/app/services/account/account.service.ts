import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Account } from '../../models/account';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private apiUrl = 'http://localhost:8085';
  constructor(private http: HttpClient) { }

  public getAccount(id: string, page: number, size: number): Observable<Array<Account>>{
    return this.http.get<Array<Account>>(this.apiUrl + '/account/' + id + '/pageOperations?page=' + page + '&size=' + size);
  }
}
