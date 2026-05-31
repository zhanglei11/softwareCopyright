import type { App, DirectiveBinding } from 'vue'
import { useAppStore } from '@/store'

export const setupPermissionDirective = (app: App) => {
  app.directive('permission', {
    mounted(el: HTMLElement, binding: DirectiveBinding<string>) {
      const store = useAppStore()
      const { value } = binding
      if (value && !store.permissions.includes(value)) {
        el.parentNode?.removeChild(el)
      }
    },
  })
}
