import type { Directive } from 'vue'
import { useAuthStore } from '@/stores/auth'

export const permission: Directive = {
  mounted(el, binding) {
    const store = useAuthStore()
    const perm = binding.value
    if (perm && !store.hasPermission(perm)) {
      el.parentNode?.removeChild(el)
    }
  },
}
