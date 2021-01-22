import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { AboutComponent } from './components/about/about.component';
import { TermsComponent } from './components/terms/terms.component';
import { RestAPIComponent } from './components/restapi/restapi.component';
import { ContactComponent } from './components/contact/contact.component';
import { SigninComponent } from './components/signin/signin.component';

import { VariantComponent } from './components/variant/variant.component';
import { GeneComponent } from './components/gene/gene.component';
import { RegionComponent } from './components/region/region.component';
import { SearchComponent } from './components/search/search.component';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    component: HomeComponent
  },
  {
    path: 'about',
    component: AboutComponent
  },
  {
    path: 'terms',
    component: TermsComponent
  },
  {
    path: 'restapi',
    component: RestAPIComponent
  },
  {
    path: 'contact',
    component: ContactComponent
  },
  {
    path: 'signin',
    component: SigninComponent
  },
  {
    path: 'variant/:query',
    component: VariantComponent
  },
  {
    path: 'gene/:query',
    component: GeneComponent
  },
  {
    path: 'region/:query',
    component: RegionComponent
  },
  {
    path: 'search/:query',
    component: SearchComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
