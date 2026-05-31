import { useAppStore } from '@/store'

export const hasPermission = (perm: string): boolean => {
  const store = useAppStore()
  const perms = store.userInfo?.perms ?? []
  return perms.includes('*:*:*') || perms.includes(perm)
}

export const hasAnyPermission = (perms: string[]): boolean =>
  perms.some(p => hasPermission(p))
