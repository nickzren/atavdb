import { Component, OnInit } from '@angular/core';
import { SampleCountService } from '../../services/sample-count.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  sampleCount: any;

  constructor(private sampleCountService: SampleCountService) { }

  ngOnInit(): void {
    this.sampleCountService.get().subscribe(data => {
      this.sampleCount = data.sampleCount;
    });
  }

}
