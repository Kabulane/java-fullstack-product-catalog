<script setup>
import { onMounted, ref } from 'vue'
import Button from 'primevue/button'
import Card from 'primevue/card'
import Column from 'primevue/column'
import DataTable from 'primevue/datatable'
import Message from 'primevue/message'
import ProgressSpinner from 'primevue/progressspinner'
import Tag from 'primevue/tag'

import { getProduct, getProductReviews } from '../api/catalogApi'

const props = defineProps({
  reference: {
    type: String,
    required: true,
  },
})

const product = ref(null)
const reviews = ref([])
const loading = ref(true)
const error = ref('')

function formatPrice() {
  if (!product.value) {
    return ''
  }

  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: product.value.currency,
  }).format(product.value.price)
}

function formatRating(rating) {
  return Number(rating).toFixed(2)
}

function formatDate(reviewedAt) {
  return new Intl.DateTimeFormat('en-US', {
    dateStyle: 'medium',
    timeStyle: 'short',
  }).format(new Date(reviewedAt))
}

function formatAuthorType(type) {
  return type.toLowerCase().replace('_', ' ')
}

async function loadProduct() {
  loading.value = true
  error.value = ''

  try {
    const [productData, reviewData] = await Promise.all([
      getProduct(props.reference),
      getProductReviews(props.reference),
    ])

    product.value = productData
    reviews.value = reviewData
  } catch {
    error.value = 'The product could not be loaded. It may not exist or the backend may be unavailable.'
  } finally {
    loading.value = false
  }
}

onMounted(loadProduct)
</script>

<template>
  <section class="product-detail">
    <RouterLink :to="{ name: 'catalog' }" class="back-link">
      <Button label="Back to catalog" icon="pi pi-arrow-left" severity="secondary" text />
    </RouterLink>

    <div v-if="loading" class="loading-state">
      <ProgressSpinner aria-label="Loading product" />
      <span>Loading product...</span>
    </div>

    <Message v-else-if="error" severity="error" :closable="false">
      {{ error }}
    </Message>

    <template v-else-if="product">
      <Card>
        <template #title>{{ product.name }}</template>
        <template #subtitle>Reference {{ product.reference }}</template>
        <template #content>
          <p class="description">{{ product.description }}</p>

          <div class="product-facts">
            <div>
              <span class="fact-label">Price</span>
              <strong>{{ formatPrice() }}</strong>
            </div>
            <div>
              <span class="fact-label">Stock</span>
              <strong>{{ product.stock }}</strong>
            </div>
            <div>
              <span class="fact-label">Reviews</span>
              <strong>{{ product.reviewCount }}</strong>
            </div>
            <div>
              <span class="fact-label">Average rating</span>
              <Tag :value="formatRating(product.averageRating)" severity="info" />
            </div>
          </div>
        </template>
      </Card>

      <Card>
        <template #title>Reviews</template>
        <template #content>
          <DataTable
            :value="reviews"
            data-key="reviewedAt"
            striped-rows
            size="small"
            table-style="min-width: 54rem"
            empty-message="This product has no reviews."
          >
            <Column field="notation" header="Rating" sortable>
              <template #body="{ data }">
                <Tag :value="String(data.notation)" severity="info" />
              </template>
            </Column>
            <Column field="reviewedAt" header="Date" sortable>
              <template #body="{ data }">{{ formatDate(data.reviewedAt) }}</template>
            </Column>
            <Column field="comment" header="Comment" />
            <Column header="Author">
              <template #body="{ data }">
                {{ data.author.firstName }} {{ data.author.lastName }}
              </template>
            </Column>
            <Column header="Type">
              <template #body="{ data }">
                <span class="author-type">{{ formatAuthorType(data.author.type) }}</span>
              </template>
            </Column>
          </DataTable>
        </template>
      </Card>
    </template>
  </section>
</template>

<style scoped>
.product-detail {
  display: grid;
  gap: 1.5rem;
}

.back-link {
  width: fit-content;
  text-decoration: none;
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

.description {
  margin-top: 0;
  line-height: 1.6;
}

.product-facts {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 1rem;
}

.product-facts > div {
  display: grid;
  gap: 0.35rem;
}

.fact-label {
  color: var(--p-text-muted-color);
  font-size: 0.875rem;
}

.product-detail :deep(.p-datatable) {
  overflow-x: auto;
}

.author-type {
  text-transform: capitalize;
}

@media (max-width: 700px) {
  .product-facts {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}
</style>
