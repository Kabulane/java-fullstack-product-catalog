import apiClient from './client'

export async function getDashboardSummary() {
  const response = await apiClient.get('/dashboard/summary')
  return response.data
}

export async function getMostAppreciatedProducts() {
  const response = await apiClient.get('/dashboard/most-appreciated-products')
  return response.data
}

export async function getLowestRatedProducts() {
  const response = await apiClient.get('/dashboard/lowest-rated-products')
  return response.data
}
