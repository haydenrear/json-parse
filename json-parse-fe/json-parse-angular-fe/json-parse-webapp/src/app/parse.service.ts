import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {RequestClass} from "./form/form.component";

@Injectable({
  providedIn: 'root'
})
export class ParseService {

  constructor(private http: HttpClient) {
  }

  sendRequest(toParse: string): Observable<RequestClass> {
    return this.http.post<RequestClass>('/parseJsonRequest', toParse, {withCredentials: true});
  }

}
