import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup } from '@angular/forms';
import { SampleCountService } from '../../services/sample-count.service';
import { SearchService } from '../../services/search.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  error: string;

  // search form
  query: string;
  phenotype: string;
  maf: string;
  isHighQualityVariant: string;
  isUltraRareVariant: string;
  isPublicAvailable: string;

  searchForm: FormGroup;
  loading = false;

  // total sample count
  sampleCount: any;

  // predfined list
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
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private sampleCountService: SampleCountService,
    private searchService: SearchService) {

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

    this.route.paramMap.subscribe(
      params => {
        this.query = params.get('query');
      });

    this.phenotype = this.route.snapshot.queryParams['phenotype'];
    this.maf = this.route.snapshot.queryParams['maf'];
    this.isHighQualityVariant = this.route.snapshot.queryParams['isHighQualityVariant'] == 'true' ? 'true' : '';
    this.isUltraRareVariant = this.route.snapshot.queryParams['isUltraRareVariant'] == 'true' ? 'true' : '';
    this.isPublicAvailable = this.route.snapshot.queryParams['isPublicAvailable'] == 'true' ? 'true' : '';
  }

  // convenience getter for easy access to form fields
  get f() { return this.searchForm.controls; }

  onSubmit() {
    if (this.f.query.value) {
      this.loading = true;

      console.log(this.f.isHighQualityVariant.value)

      this.searchService.querytype(this.f.query.value).subscribe(
        data => {
          // Hack: reload component on same URL navigation
          // this.router.navigateByUrl('/', {skipLocationChange: true}).then(()=>this.router.navigate([<route>]));
          this.router.navigateByUrl('/', { skipLocationChange: true})
            .then(() => this.router.navigate([`${data.querytype}/${this.f.query.value}`], 
            { queryParams: 
              { 
                phenotype: this.f.phenotype.value,
                maf: this.f.maf.value,
                isHighQualityVariant: this.f.isHighQualityVariant.value,
                isUltraRareVariant: this.f.isUltraRareVariant.value,
                isPublicAvailable: this.f.isPublicAvailable.value
              }
            }));

          this.loading = false;
        },
        error => {
          if (error.error) {
            this.error = error.error.message;
          } else {
            this.error = "Unexpected error";
          }
          this.loading = false;
          this.router.navigate(['/'], { queryParams: { error: this.error } });
        }
      );
    }
  }
}
