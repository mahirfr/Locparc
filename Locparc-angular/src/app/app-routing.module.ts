import { NgModule                      } from '@angular/core'                                       ;
import { RouterModule         , Routes } from '@angular/router'                                     ;
import { HomeComponent                 } from './pages/home/home.component'                         ;
import { LoginComponent                } from './pages/login/login.component'                       ;
import { UserGuard                     } from './guards/user.guard'                                 ;
import { ShoppingCartComponent         } from './utils/shopping-cart/shopping-cart.component'       ;
import { EquipementComponent           } from './pages/equipement/equipement.component'             ;
import { AccessDeniedComponent         } from './pages/errors/access-denied/access-denied.component';
import { LenderGuard                   } from './guards/lender.guard'                               ;
import { OrdersComponent               } from './pages/orders/orders.component'                     ;
import { UsersComponent                } from './pages/users/users.component'                       ;
import { AdminGuard                    } from './guards/admin.guard'                                ;
import { AuthGuard                     } from './guards/auth.guard'                                 ;
import { ProfileComponent              } from './pages/profile/profile.component'                   ;

// TODO: Add routes for new components
// TODO: Add route for not found page

const routes: Routes = [
  {
    path: "home", 
    component: HomeComponent, 
    canActivate: [UserGuard]
  },
  {
    path: "", 
    redirectTo: "home",
    pathMatch: "full"
  },
  {
    path: "login",
    component: LoginComponent,
    canActivate: [AuthGuard]
  },
  {
    path: "orders",
    component: OrdersComponent,
    canActivate: [UserGuard]
  },
  {
    path: "profile",
    component: ProfileComponent,
    canActivate: [UserGuard] 
  },
  {
    path: "shopping-cart", 
    component: ShoppingCartComponent, 
    canActivate: [UserGuard]
  },
  {
    path: "equipement",
    component: EquipementComponent,
    canActivate: [AdminGuard]
  },
  {
    path: "utilisateurs",
    component: UsersComponent,
    canActivate: [AdminGuard]
  },
  {
    path: "**",
    component: AccessDeniedComponent,
    canActivate: [AuthGuard]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
