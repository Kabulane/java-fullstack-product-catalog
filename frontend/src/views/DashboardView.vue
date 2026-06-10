<script setup>
import { onMounted, ref } from 'vue'
import Card from 'primevue/card'
import Column from 'primevue/column'
import DataTable from 'primevue/datatable'
import Message from 'primevue/message'
import ProgressSpinner from 'primevue/progressspinner'
import Tag from 'primevue/tag'

import {
  getDashboardSummary,
  getLowestRatedProducts,
  getMostAppreciatedProducts,
} from '../api/dashboardApi'

const summary = ref({
  totalProducts: 0,
  totalReviews: 0,
})
const mostAppreciatedProducts = ref([])
const lowestRatedProducts = ref([])
const loading = ref(true)
const error = ref('')

function formatPrice(product) {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: product.currency,
  }).format(product.price)
}

function formatRating(rating) {
  return Number(rating).toFixed(2)
}

async function loadDashboard() {
  loading.value = true
  error.value = ''

  try {
    const [summaryData, mostAppreciatedData, lowestRatedData] = await Promise.all([
      getDashboardSummary(),
      getMostAppreciatedProducts(),
      getLowestRatedProducts(),
    ])

    summary.value = summaryData
    mostAppreciatedProducts.value = mostAppreciatedData
    lowestRatedProducts.value = lowestRatedData
  } catch {
    error.value = 'The dashboard data could not be loaded. Please verify that the backend is running.'
  } finally {
    loading.value = false
  }
}

onMounted(loadDashboard)
</script>

<template>
  <section class="dashboard">
    <h1>Dashboard</h1>

    <div v-if="loading" class="loading-state">
      <ProgressSpinner aria-label="Loading dashboard" />
      <span>Loading dashboard...</span>
    </div>

    <Message v-else-if="error" severity="error" :closable="false">
      {{ error }}
    </Message>

    <template v-else>
      <div class="kpi-grid">
        <Card>
          <template #title>Total products</template>
          <template #content>
            <strong class="kpi-value">{{ summary.totalProducts }}</strong>
          </template>
        </Card>

        <Card>
          <template #title>Total reviews</template>
          <template #content>
            <strong class="kpi-value">{{ summary.totalReviews }}</strong>
          </template>
        </Card>
      </div>

      <div class="table-grid">
        <Card>
          <template #title>Most appreciated products</template>
          <template #subtitle>Top five by number of positive reviews</template>
          <template #content>
            <DataTable
              :value="mostAppreciatedProducts"
              data-key="reference"
              striped-rows
              size="small"
              table-style="min-width: 42rem"
            >
              <Column field="name" header="Product" />
              <Column field="price" header="Price">
                <template #body="{ data }">{{ formatPrice(data) }}</template>
              </Column>
              <Column field="reviewCount" header="Reviews" />
              <Column field="averageRating" header="Average">
                <template #body="{ data }">
                  <Tag :value="formatRating(data.averageRating)" severity="success" />
                </template>
              </Column>
              <Column field="positiveReviewCount" header="Positive" />
            </DataTable>
          </template>
        </Card>

        <Card>
          <template #title>Lowest-rated products</template>
          <template #subtitle>Five lowest averages below 3</template>
          <template #content>
            <DataTable
              :value="lowestRatedProducts"
              data-key="reference"
              striped-rows
              size="small"
              table-style="min-width: 36rem"
            >
              <Column field="name" header="Product" />
              <Column field="price" header="Price">
                <template #body="{ data }">{{ formatPrice(data) }}</template>
              </Column>
              <Column field="reviewCount" header="Reviews" />
              <Column field="averageRating" header="Average">
                <template #body="{ data }">
                  <Tag :value="formatRating(data.averageRating)" severity="danger" />
                </template>
              </Column>
            </DataTable>
          </template>
        </Card>
      </div>
    </template>
  </section>
</template>

<style scoped>
.dashboard {
  display: grid;
  gap: 1.5rem;
}

.dashboard h1 {
  margin-bottom: 0;
}

.loading-state {
  display: flex;
  min-height: 16rem;
  align-items: center;
  justify-content: center;
  gap: 1rem;
}

.loading-state :deep(.p-progressspinner) {
  width: 2.5rem;
  height: 2.5rem;
}

.kpi-grid,
.table-grid {
  display: grid;
  gap: 1rem;
}

.kpi-grid {
  grid-template-columns: repeat(2, minmax(0, 1fr));
}

.table-grid {
  grid-template-columns: repeat(2, minmax(0, 1fr));
}

.kpi-value {
  font-size: 2rem;
  color: var(--p-primary-color);
}

.table-grid :deep(.p-card-body) {
  height: 100%;
}

.table-grid :deep(.p-card-content) {
  overflow-x: auto;
}

@media (max-width: 900px) {
  .table-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 560px) {
  .kpi-grid {
    grid-template-columns: 1fr;
  }
}
</style>
