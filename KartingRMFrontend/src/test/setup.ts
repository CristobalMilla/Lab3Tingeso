import '@testing-library/jest-dom'
import { beforeEach, vi } from 'vitest'

beforeEach(() => {
  // Clear all mocks before each test
  vi.clearAllMocks()
})

// Mock IntersectionObserver for components that might use it
class IntersectionObserver {
  constructor() {}
  disconnect() {}
  observe() {}
  unobserve() {}
}

// @ts-expect-error - Global assignment for testing environment
globalThis.IntersectionObserver = IntersectionObserver
