import { Component } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';

declare let gtag: Function;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontend';

  constructor(public router: Router) {
    // Google Analytics track the URL a user has navigated to
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        gtag('config', 'UA-32885253-12', { 'page_path': event.urlAfterRedirects });
      }
    })
  }
}