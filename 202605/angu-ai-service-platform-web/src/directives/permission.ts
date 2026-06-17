import type { App, DirectiveBinding } from 'vue'
import { hasPermission } from '@/utils/permission'

export default {
  install(app: App) {
    app.directive('permission', {
      mounted(el: HTMLElement, binding: DirectiveBinding) {
        const { value } = binding
        if (value && !hasPermission(value)) {
          el.parentNode?.removeChild(el)
        }
      },
    })
  },
}
