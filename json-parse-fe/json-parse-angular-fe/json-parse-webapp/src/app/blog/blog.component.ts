import { Component, OnInit } from '@angular/core';
import {PostComponent} from "../post/post.component";

@Component({
  selector: 'app-blog',
  templateUrl: './blog.component.html',
  styleUrls: ['./blog.component.css', '../app.component.css']
})
export class BlogComponent implements OnInit {

  posts: PostComponent[];

  constructor() {
    this.posts = []
  }

  ngOnInit(): void {
  }

}
