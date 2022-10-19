import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { MatSliderModule } from '@angular/material/slider';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from "@angular/router";
import { MatTabsModule } from '@angular/material/tabs'
import {RoutingRoutingModule} from "./routing/routing.module";
import {MatCardModule} from '@angular/material/card';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatSelectModule} from "@angular/material/select";
import {MatInputModule} from "@angular/material/input";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatButtonModule} from "@angular/material/button";
import { FormComponent } from './form/form.component';
import { LoginComponent } from './login/login.component';
import { MenuComponent } from './menu/menu.component';
import { PostComponent } from './post/post.component';
import { AboutComponent } from './about/about.component';
import { BlogComponent } from './blog/blog.component';
import {MatMenuModule} from "@angular/material/menu";
import {IAppState, INITIAL_STATE, rootReducer} from "./store";
import { StoreModule } from '@ngrx/store';

@NgModule({
  declarations: [
    AppComponent,
    FormComponent,
    LoginComponent,
    MenuComponent,
    PostComponent,
    AboutComponent,
    BlogComponent
  ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        MatSliderModule,
        RouterModule,
        MatTabsModule,
        RoutingRoutingModule,
        MatCardModule,
        HttpClientModule,
        MatFormFieldModule,
        MatSelectModule,
        MatInputModule,
        FormsModule,
        MatButtonModule,
        ReactiveFormsModule,
        MatMenuModule,
        StoreModule.forRoot({}, {}),
    ],
  providers: [HttpClient],
  bootstrap: [AppComponent]
})
export class AppModule {
}
