import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { DataTableDirective } from 'angular-datatables';
import { FormBuilder, FormGroup } from '@angular/forms';
import { SampleCountService } from '../../services/sample-count.service';
import { SearchService } from '../../services/search.service';
import { Subject } from 'rxjs';


@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements AfterViewInit, OnDestroy, OnInit {
  // search form
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

  // search result
  variants: Array<any>;

  // datatables
  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;
  isDtInitialized: boolean = false;
  dtOptions: any = {};
  dtTrigger: Subject<any> = new Subject();

  constructor(
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

    this.dtOptions = {
      pagingType: 'simple_numbers',
      pageLength: 10,
      processing: true,
      dom: 'Blfrtip',
      buttons: [
        {
          extend: 'csv',
          text: '<i class="fas fa-file-csv"></i> Download',
          className: 'btn btn-light'
        }
      ]
    };
  }

  ngAfterViewInit(): void {
    this.dtTrigger.next();
  }

  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }

  // convenience getter for easy access to form fields
  get f() { return this.searchForm.controls; }

  onSubmit() {
    this.loading = true;

    this.searchService.search(this.f.query.value).subscribe(
      data => {
        this.variants = data;
        this.loading = false;

        this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
          dtInstance.destroy();
          this.dtTrigger.next();
        });
      }
    );
  }
}
