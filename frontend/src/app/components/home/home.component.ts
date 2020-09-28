import { Component, OnInit } from '@angular/core';
import { SampleCountService } from '../../services/sample-count.service';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  searchForm: FormGroup;
  loading = false;
  sampleCount: any;
  MAF_LIST = ["", "0.01", "0.005", "0.001"];
  PHENOTYPE_LIST = ["", "amyotrophic lateral sclerosis",
    "autoimmune disease", "bone disease", "brain malformation", "cancer", "cardiovascular disease",
    "congenital disorder", "control", "control mild neuropsychiatric disease", "covid-19",
    "dementia", "dermatological disease", "diseases that affect the ear",
    "endocrine disorder", "epilepsy", "febrile seizures", "fetal ultrasound anomaly",
    "gastrointestinal disease", "healthy family member", "hematological disease",
    "infectious disease", "intellectual disability", "kidney and urological disease",
    "liver disease", "metabolic disease", "neurodegenerative", "nonhuman", "obsessive compulsive disorder",
    "ophthalmic disease", "other", "other neurodevelopmental disease", "other neurological disease",
    "other neuropsychiatric disease", "primary immune deficiency", "pulmonary disease",
    "schizophrenia", "sudden death", "alzheimers disease", "cerebral palsy"];

  constructor(
    private formBuilder: FormBuilder,
    private sampleCountService: SampleCountService) {
    this.searchForm = this.formBuilder.group({
      query: [''],
      phenotype: [''],
      maf: [''],
      isHighQualityVariant: [false],
      isUltraRareVariant: [false],
      isPublicAvailable: [false]
    });
  }

  ngOnInit(): void {
    this.sampleCountService.get().subscribe(data => {
      this.sampleCount = data.sampleCount;
    });
  }

  // convenience getter for easy access to form fields
  get f() { return this.searchForm.controls; }

  onSubmit() {
    this.loading = true;

    console.log(this.f.query.value);
    console.log(this.f.phenotype.value);
    console.log(this.f.maf.value);
    console.log(this.f.isHighQualityVariant.value);
    console.log(this.f.isUltraRareVariant.value);
    console.log(this.f.isPublicAvailable.value);
  }
}
