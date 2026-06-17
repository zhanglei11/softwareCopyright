import type { App, Directive } from 'vue'
import { useAppStore } from '@/store'

const permissionDirective: Directive = {
  mounted(el, binding) {
    const store = useAppStore()
    const required = binding.value
    if (!required) return
    const perms = store.roles || []
    const has = Array.isArray(required)
      ? required.some(p => perms.includes(p))
      : perms.includes(required)
    if (!has) {
      el.parentNode?.removeChild(el)
    }
  },
}

export default {
  install(app: App) {
    app.directive('permission', permissionDirective)
  },
}
