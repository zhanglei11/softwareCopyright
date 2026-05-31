import { useUserStore } from '@/store'
export function hasPermission(p: string): boolean {
  if (!p) return true
  const s = useUserStore()
  return (s.roles || []).includes('SUPER_ADMIN') || (s.permissions || []).includes('*:*:*') || (s.permissions || []).includes(p)
}
