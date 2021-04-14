import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AccountService } from '../../services/account.service';
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
  querytype: string;
  query: string;
  phenotype: string;
  maf: string;
  isHighQualityVariant: string;
  isUltraRareVariant: string;
  isPubliclyAvailable: string;
  experimentId: string;

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
    public accountService: AccountService,
    private sampleCountService: SampleCountService,
    private searchService: SearchService) {

    this.searchForm = this.formBuilder.group({
      query: [''],
      phenotype: [''],
      maf: [''],
      isHighQualityVariant: [false],
      isUltraRareVariant: [false],
      isPubliclyAvailable: [false],
      experimentId: ['']
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(
      params => {
        this.query = params.get('query');
      });

    this.phenotype = this.route.snapshot.queryParams['phenotype'];
    this.maf = this.route.snapshot.queryParams['maf'];
    this.isHighQualityVariant = this.route.snapshot.queryParams['isHighQualityVariant'] == 'true' ? 'true' : null;
    this.isUltraRareVariant = this.route.snapshot.queryParams['isUltraRareVariant'] == 'true' ? 'true' : null;
    this.isPubliclyAvailable = this.route.snapshot.queryParams['isPubliclyAvailable'] == 'true' ? 'true' : null;
    this.experimentId = this.route.snapshot.queryParams['experimentId'];

    // gene or region search high quality variants only
    if (this.query) {
      this.searchService.querytype(this.query).subscribe(
        data => {
          if (data.querytype == 'gene' || data.querytype == 'region') {
            this.isHighQualityVariant = 'true';
          }
        }
      )

      // unauthenticated user only allow search public avaiable data
      if (!this.accountService.isAuthenticated()) {
        this.isPubliclyAvailable = 'true';
      }
    }

    // init sample count
    this.sampleCountService.get(this.phenotype, this.isPubliclyAvailable).subscribe(
      data => {
        this.sampleCount = data.sampleCount;
      });
  }

  // convenience getter for easy access to form fields
  get f() { return this.searchForm.controls; }

  onSubmit() {
    if (this.f.query.value) {
      this.loading = true;

      this.searchService.querytype(this.f.query.value).subscribe(
        data => {
          // Hack: reload component on same URL navigation
          // this.router.navigateByUrl('/', {skipLocationChange: true}).then(()=>this.router.navigate([<route>]));
          this.router.navigateByUrl('/', { skipLocationChange: true })
            .then(() => this.router.navigate([`${data.querytype}/${this.f.query.value}`],
              {
                queryParams:
                {
                  phenotype: this.f.phenotype.value,
                  maf: this.f.maf.value,
                  isHighQualityVariant: this.f.isHighQualityVariant.value ? true : null,
                  isUltraRareVariant: this.f.isUltraRareVariant.value ? true : null,
                  isPubliclyAvailable: this.f.isPubliclyAvailable.value ? true : null,
                  experimentId: this.f.experimentId.value
                }
              }));

          this.querytype = data.querytype;
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
