import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {ParseService} from "../parse.service";
import {map} from "rxjs/operators";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.css']
})
export class FormComponent implements OnInit {

  response: string[] = [];
  parseForm = new FormGroup({
    toParse: new FormControl('')
  });

  constructor(private parseService: ParseService) { }

  ngOnInit(): void {
  }

  sendParse(): void {
    this.parseService.sendRequest(this.parseForm.get("toParse")?.value)
      .subscribe(response => {
        console.log(response);
        while(this.response.length != 0){
          this.response.pop()
        }
        this.response.push(response.request);
      });
  }
}

export interface RequestClass {
  request: string;
}
