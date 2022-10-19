import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {

  constructor() {
    this._text = ""
  }

  _text: string;

  set text(text: string){
    this._text = text;
  }

  get text(){
    return this._text;
  }

  ngOnInit(): void {
  }

}
