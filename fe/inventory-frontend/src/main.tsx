import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import {InventoryApp} from './App.tsx'

createRoot(document.getElementById('root')!).render(
    <StrictMode>
        <InventoryApp/>
    </StrictMode>,
)
