import { createRouter, createWebHistory } from 'vue-router'

import CatalogView from '../views/CatalogView.vue'
import DashboardView from '../views/DashboardView.vue'
import ProductDetailView from '../views/ProductDetailView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/dashboard',
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: DashboardView,
    },
    {
      path: '/catalog',
      name: 'catalog',
      component: CatalogView,
    },
    {
      path: '/catalog/:reference',
      name: 'product-detail',
      component: ProductDetailView,
      props: true,
    },
  ],
})

export default router
