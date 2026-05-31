import { useAppStore as useUserStore } from '@/store'

export function hasPermission(permission: string): boolean {
  const store = useUserStore()
  if (!permission) return true
  const perms = store.permissions || []
  const roles = store.roles || []
  return roles.includes('SUPER_ADMIN') ||
    perms.includes('*:*:*') ||
    perms.includes(permission)
}

export function hasRole(role: string): boolean {
  const store = useUserStore()
  const roles = store.roles || []
  return roles.includes(role)
}
