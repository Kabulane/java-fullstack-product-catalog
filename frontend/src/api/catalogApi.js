import apiClient from './client'

export async function getProducts() {
  const response = await apiClient.get('/products')
  return response.data
}

export async function getProduct(reference) {
  const response = await apiClient.get(`/products/${encodeURIComponent(reference)}`)
  return response.data
}

export async function getProductReviews(reference) {
  const response = await apiClient.get(`/products/${encodeURIComponent(reference)}/reviews`)
  return response.data
}
