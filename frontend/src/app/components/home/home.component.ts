import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  error: string;

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.error = this.route.snapshot.queryParams['error'];
  }
}