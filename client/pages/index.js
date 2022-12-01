import Head from 'next/head'
import Link from 'next/link'
import styles from '../styles/Home.module.css'

export default function Home() {
  return (
    <div className={styles.container}>
      <Head>
        <title>UBAffets | Home</title>
        <meta name="description" content="Apiclación web revolucionaria para el buffet de la FIUBA" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <h1>Bienvenid@ a la app web del buffet</h1>
      <Link href="pedido">Ver Pedido</Link>
    </div>
  )
}