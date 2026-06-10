<script setup>
import Card from 'primevue/card'

defineProps({
  reference: {
    type: String,
    required: true,
  },
})
</script>

<template>
  <section>
    <h1>Product details</h1>

    <Card>
      <template #title>Product {{ reference }}</template>
      <template #content>
        Product information and reviews will be displayed here.
      </template>
    </Card>
  </section>
</template>
