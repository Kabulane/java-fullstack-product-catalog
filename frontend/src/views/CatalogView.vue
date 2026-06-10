<script setup>
import { onMounted, ref } from 'vue'
import Button from 'primevue/button'
import Column from 'primevue/column'
import DataTable from 'primevue/datatable'
import Message from 'primevue/message'
import ProgressSpinner from 'primevue/progressspinner'
import Tag from 'primevue/tag'

import { getProducts } from '../api/catalogApi'

const products = ref([])
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

async function loadProducts() {
  loading.value = true
  error.value = ''

  try {
    products.value = await getProducts()
  } catch {
    error.value = 'The product catalogue could not be loaded. Please verify that the backend is running.'
  } finally {
    loading.value = false
  }
}

onMounted(loadProducts)
</script>

<template>
  <section class="catalog">
    <h1>Catalog</h1>

    <div v-if="loading" class="loading-state">
      <ProgressSpinner aria-label="Loading catalogue" />
      <span>Loading catalogue...</span>
    </div>

    <Message v-else-if="error" severity="error" :closable="false">
      {{ error }}
    </Message>

    <DataTable
      v-else
      :value="products"
      data-key="reference"
      paginator
      :rows="10"
      :rows-per-page-options="[10, 25, 50]"
      striped-rows
      size="small"
      table-style="min-width: 62rem"
    >
      <Column field="reference" header="Reference" sortable />
      <Column field="name" header="Name" sortable />
      <Column field="price" header="Price" sortable>
        <template #body="{ data }">{{ formatPrice(data) }}</template>
      </Column>
      <Column field="stock" header="Stock" sortable />
      <Column field="reviewCount" header="Reviews" sortable />
      <Column field="averageRating" header="Average" sortable>
        <template #body="{ data }">
          <Tag :value="formatRating(data.averageRating)" severity="info" />
        </template>
      </Column>
      <Column header="Action">
        <template #body="{ data }">
          <RouterLink
            :to="{ name: 'product-detail', params: { reference: data.reference } }"
            class="detail-link"
          >
            <Button label="View" icon="pi pi-eye" size="small" />
          </RouterLink>
        </template>
      </Column>
    </DataTable>
  </section>
</template>

<style scoped>
.catalog {
  display: grid;
  gap: 1.5rem;
}

.catalog h1 {
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

.catalog :deep(.p-datatable) {
  overflow-x: auto;
}

.detail-link {
  text-decoration: none;
}
</style>
