import { useAppStore } from '@/store'

export const hasPermission = (permission: string): boolean => {
  const store = useAppStore()
  if (!permission) return true
  return store.permissions.includes(permission)
}

export const hasAnyPermission = (...permissions: string[]): boolean => {
  return permissions.some(hasPermission)
}
