import type { Directive } from 'vue'
import { hasPermission } from '@/utils/permission'

export const vPermission: Directive<HTMLElement, string> = {
  mounted(el, binding) {
    if (!hasPermission(binding.value)) {
      el.parentNode?.removeChild(el)
    }
  }
}
