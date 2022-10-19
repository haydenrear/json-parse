import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  notLoggedIn: boolean = true;

  loggedInBroad: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(true)

  constructor(private http: HttpClient) { }

  notLoggedInFunc(){
    this.http.get<boolean>("/isLoggedIn")
      .pipe(
        map(returnVal => {
          if(returnVal === true || returnVal === false){
            console.log(returnVal);
            this.notLoggedIn =  returnVal;
            this.loggedInBroad.next(returnVal);
            return returnVal
          }
          else {
            return true;
          }
        })
      )
      .subscribe();
    return this.notLoggedIn;
  }

  checkLocation() {
    return navigator.geolocation.getCurrentPosition;
  }
}
