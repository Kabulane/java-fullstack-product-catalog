<script setup>
import Menubar from 'primevue/menubar'

const navigationItems = [
  {
    label: 'Dashboard',
    icon: 'pi pi-chart-bar',
    route: '/dashboard',
  },
  {
    label: 'Catalog',
    icon: 'pi pi-list',
    route: '/catalog',
  },
]
</script>

<template>
  <div class="app-shell">
    <Menubar :model="navigationItems">
      <template #start>
        <span class="app-title">Product Catalog</span>
      </template>

      <template #item="{ item, props }">
        <RouterLink
          v-if="item.route"
          v-slot="{ href, navigate }"
          :to="item.route"
          custom
        >
          <a :href="href" v-bind="props.action" @click="navigate">
            <span :class="item.icon" />
            <span>{{ item.label }}</span>
          </a>
        </RouterLink>
      </template>
    </Menubar>

    <main class="page-container">
      <RouterView />
    </main>
  </div>
</template>
