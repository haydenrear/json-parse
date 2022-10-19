import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {FormComponent} from "../form/form.component";
import {LoginComponent} from "../login/login.component";
import {BlogComponent} from "../blog/blog.component";
import {AboutComponent} from "../about/about.component";


const routes: Routes = [
  {path: 'parse', component: FormComponent},
  {path: 'login', component: LoginComponent},
  {path: 'blog', component: BlogComponent},
  {path: 'about', component: AboutComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class RoutingRoutingModule {
}
