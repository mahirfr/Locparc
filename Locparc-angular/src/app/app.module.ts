import { NgModule                                       } from '@angular/core'                                        ;
import { BrowserModule                                  } from '@angular/platform-browser'                            ;
import { BrowserAnimationsModule                        } from '@angular/platform-browser/animations'                 ;
import { AppRoutingModule                               } from './app-routing.module'                                 ;
import { AppComponent                                   } from './app.component'                                      ;
import { HomeComponent                                  } from './pages/home/home.component'                          ;
import { LoginComponent                                 } from './pages/login/login.component'                        ;
import { MatButtonModule                                } from '@angular/material/button'                             ;
import { MatCardModule                                  } from '@angular/material/card'                               ;
import { MatIconModule                                  } from '@angular/material/icon'                               ;
import { MatFormFieldModule                             } from '@angular/material/form-field'                         ;
import { FormsModule              , ReactiveFormsModule } from '@angular/forms'                                       ;
import { MatInputModule                                 } from '@angular/material/input'                              ;
import { MatSelectModule                                } from '@angular/material/select'                             ;
import { HTTP_INTERCEPTORS        , HttpClientModule    } from '@angular/common/http'                                 ;
import { SearchBarComponent                             } from './utils/search-bar/search-bar.component'              ;
import { JwtInterceptor                                 } from './jwt.interceptor'                                    ;
import { ShoppingCartComponent                          } from './utils/shopping-cart/shopping-cart.component'        ;
import { EquipementComponent                            } from './pages/equipement/equipement.component'              ;
import { MatCheckboxModule                              } from '@angular/material/checkbox'                           ;
import { PlanningComponent                              } from './utils/planning/planning.component'                  ;
import { MatTableModule                                 } from '@angular/material/table'                              ;
import { AccessDeniedComponent                          } from './pages/errors/access-denied/access-denied.component' ;
import { OrdersComponent                                } from './pages/orders/orders.component'                      ;
import { UsersComponent                                 } from './pages/users/users.component'                        ;
import { ProfileComponent                               } from './pages/profile/profile.component'                    ;
import { MatTabsModule                                  } from '@angular/material/tabs'                               ;
import { FormControlForceTypePipe                       } from './pipes/form-control-force-type.pipe'                 ;
import { MatPaginatorModule                             } from '@angular/material/paginator'                          ;
import { MatDialogModule                                } from '@angular/material/dialog'                             ;
import { DialogComponent                                } from './utils/dialogs/dialog/dialog.component'                      ;
import { MAT_DATE_LOCALE, MatNativeDateModule                            } from '@angular/material/core'                               ;
import { MatDatepickerModule                            } from '@angular/material/datepicker';
import { DateRangePickerComponent } from './utils/date-range-picker/date-range-picker.component'                         ;
import { DatePipe } from '@angular/common';
import { SpinnerComponent } from './utils/spinner/spinner.component';
import { LoadingInterceptor } from './loading.interceptor';
import { MyOrdersComponent } from './utils/my-orders/my-orders.component';
import { SearchOrdersComponent } from './utils/search-orders/search-orders.component';
import { OrderHistoryComponent } from './utils/order-history/order-history.component';
import { DialogConfirmationComponent } from './utils/dialogs/dialog-confirmation/dialog-confirmation.component';
import { OrderDialogComponent } from './utils/dialogs/order-dialog/order-dialog.component';

@NgModule({
  declarations: [
    AppComponent            ,
    HomeComponent           ,
    LoginComponent          ,
    SearchBarComponent      ,
    ShoppingCartComponent   ,
    EquipementComponent     ,
    PlanningComponent       ,
    AccessDeniedComponent   ,
    OrdersComponent         ,
    UsersComponent          ,
    ProfileComponent        ,
    FormControlForceTypePipe,
    DialogComponent,
    DateRangePickerComponent,
    SpinnerComponent,
    MyOrdersComponent,
    SearchOrdersComponent,
    OrderHistoryComponent,
    DialogConfirmationComponent,
    OrderDialogComponent
  ],
  imports: [
    BrowserModule          ,
    AppRoutingModule       ,
    BrowserAnimationsModule,
    MatButtonModule        ,
    MatCardModule          ,
    HttpClientModule       ,
    MatIconModule          ,
    MatFormFieldModule     ,
    FormsModule            ,
    ReactiveFormsModule    ,
    MatInputModule         ,
    MatSelectModule        ,
    MatCheckboxModule      ,
    MatTableModule         ,
    MatTabsModule          ,
    MatDialogModule        ,
    MatNativeDateModule    ,
    MatPaginatorModule     ,
    MatDatepickerModule
  ],
  providers: [{provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
              { provide: MAT_DATE_LOCALE, useValue: 'fr-FR' },
              { provide: HTTP_INTERCEPTORS, useClass: LoadingInterceptor, multi: true },
              DatePipe],
  bootstrap: [AppComponent]
})
export class AppModule { }
